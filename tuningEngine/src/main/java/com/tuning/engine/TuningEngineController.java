/*
 * 06.04.2013 | 11:37:52
 * Marcel Wieczorek
 * marcel@wieczorek-it.de
 */
package com.tuning.engine;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.primitives.UnsignedBytes;
import com.tuning.engine.constants.Country;
import com.tuning.engine.data.Item;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.*;

/**
 * @author Marcel Wieczorek
 * @version 1.0
 * @since 1.0
 */
public class TuningEngineController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(TuningEngineController.class);

    private Country currentCountry = Country.GERMANY;

    @FXML
    private Menu menuFile, menuEdit, menuCountry, menuHelp;
    @FXML
    private Label lblCountry, lblDate, lblTime;

    @FXML
    private TextField xFrom, xTo;
    @FXML
    private LineChart<Integer, Integer> lineChart;
    @FXML
    private NumberAxis xNumberAxis, yNumberAxis;
    @FXML
    private TableView<Data<Integer, Integer>> tableView;
    @FXML
    private Slider sliderChart;
    @FXML
    private TableColumn tableColumnX, tableColumnY;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ChoiceBox binaryType;
    @FXML
    private CheckBox signed;

    @FXML
    public void openFile() {

        File file;
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MM files (*.mm)", "*.mm", "*.128", "*.*", "*");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show open file dialog
        file = fileChooser.showOpenDialog(null);


        String path = "/Users/waveburn/tuningengine/tuningEngine/src/main/resources/tuningMap/deca";

        if (file == null) {
            file = new File(path);
        }

        byte[] bytes = null;
        try {
            bytes = Files.toByteArray(file);
        } catch (IOException e) {
            LOG.error(e.getMessage(), "File not found or blocked from another application");
        }

        Preconditions.checkNotNull(bytes, "Selected file can´t be opened, contact Marcel ;)");
        List<Item> itemList = Lists.newArrayList();
        for (int i = 0; i < bytes.length; i++) {

            // progressBar.setProgress((bytes.length/100)*i);
            // progressIndicator.setProgress(progressBar.getProgress());

            Item item = new Item();
            item.setKey(i);

            Integer byteForChart;
            if (signed.isSelected()) {
                Byte byteForConversion = bytes[i];
                Byte convertedByte = Byte.parseByte(byteForConversion.toString(), 10);
                byteForChart = convertedByte.intValue();
            } else {
                byteForChart = UnsignedBytes.toInt(bytes[i]);
            }

            item.setValue(byteForChart);

            itemList.add(item);
        }

        // doesn´t work.. try to update chart after thread runs
        // openFileWithProgress(bytes, itemList);

        initLinechart(itemList);
    }

    public void openFileWithProgress(final byte[] bytes, final List<Item> itemList) {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                for (int i = 0; i < bytes.length; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        LOG.error(e.getMessage(), "Thread for progress bar error");
                    }
                    Item item = new Item();
                    item.setKey(i);

                    Byte byteForConversion = bytes[i];
                    Byte convertedByte = Byte.parseByte(byteForConversion.toString(), 10);
                    Integer decimalValue = convertedByte.intValue();
                    item.setValue(decimalValue);

                    itemList.add(item);

                    System.out.println(i);
                    updateProgress(i, bytes.length / 100);
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        progressIndicator.progressProperty().bind(task.progressProperty());

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    @FXML
    public void showDate() {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, currentCountry.getLocale());

        lblDate.setText(dateFormat.format(new Date()));
    }

    @FXML
    public void showTime() {
        final DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.FULL, currentCountry.getLocale());

        lblTime.setText(timeFormat.format(new Date()));
    }

    @FXML
    public void switchCountry(final ActionEvent e) {
        final MenuItem target = (MenuItem) e.getTarget();

        if (currentCountry != target.getUserData()) {
            currentCountry = (Country) target.getUserData();

            updateCountry();
        }
    }

    @FXML
    public void resetChartRange(final ActionEvent e) {
        xNumberAxis.setAutoRanging(true);
        yNumberAxis.setAutoRanging(true);
    }

    @FXML
    public void changeChartRange(final ActionEvent e) {
        LOG.info("change range");
        xNumberAxis.setAutoRanging(false);

        double xF = 0;
        CharSequence csF = xFrom.getCharacters();
        if (csF != null && csF.length() > 0) {
            xF = Double.parseDouble(csF.toString());
        }

        double xT = 0;
        CharSequence csT = xTo.getCharacters();
        if (csT != null && csT.length() > 0) {
            xT = Double.parseDouble(csT.toString());
        }

        xNumberAxis.setUpperBound(xT);
        xNumberAxis.setLowerBound(xF);
    }

    @FXML
    public void onMouseDragged(final MouseEvent e) {
        @SuppressWarnings("unchecked")
        final LineChart<Number, Number> lineChart = (LineChart<Number, Number>) e.getSource();

        LOG.info("mouse dragged on lineChart: {}", lineChart);
    }

    private void updateCountry() {
        showDate();

        showTime();

        for (final MenuItem menuItem : menuCountry.getItems()) {
            menuItem.setDisable(currentCountry == menuItem.getUserData());

            final Locale locale = ((Country) menuItem.getUserData()).getLocale();
            menuItem.setText(locale.getDisplayCountry(currentCountry.getLocale()));
        }

        lblCountry.setText(currentCountry.getLocale().getDisplayCountry(currentCountry.getLocale()));
    }

    /*
     * (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    public void initialize(final URL location, final ResourceBundle resources) {
        binaryType.getSelectionModel().selectFirst();
        initCountry();
        initLinechart(null);
        initTableView();
        initSlider();
    }

    private void initSlider() {
        // Listen for Slider value changes
        sliderChart.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                //LOG.debug("change" + newValue);
                xNumberAxis.setAutoRanging(false);
                xNumberAxis.setUpperBound(newValue.intValue() + 10);
                xNumberAxis.setLowerBound(newValue.intValue() - 10);
            }
        });

    }

    private void initCountry() {
        for (final Country country : Country.values()) {
            final Locale locale = country.getLocale();

            final MenuItem menuItem = new MenuItem(locale.getDisplayCountry(currentCountry.getLocale()));
            menuItem.setUserData(country);

            menuCountry.getItems().add(menuItem);
        }

        updateCountry();
    }

    private void initLinechart(List<Item> itemList) {
        final InputStream processInputStream = getClass().getResourceAsStream("/data/process.xml");
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(com.tuning.engine.data.Process.class);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            final com.tuning.engine.data.Process process = (com.tuning.engine.data.Process) unmarshaller.unmarshal(processInputStream);

            List<Item> items = process.getItems();
            // trick so ich mache nicht komplett alles kaputt wenn die open file funkt. nicht ;)
            if (CollectionUtils.isNotEmpty(itemList)) {
                items = itemList;
            }

            final List<Data<Integer, Integer>> data = new LinkedList<>();
            for (final Item item : items) {
                final Data<Integer, Integer> d = new Data<>(item.getKey(), item.getValue());

                // add data
                data.add(d);
            }

            final ObservableList<Data<Integer, Integer>> observalObservableList = FXCollections.observableArrayList(data);
            final Series<Integer, Integer> processSeries = new Series<>(observalObservableList);

            lineChart.getData().add(processSeries);

            for (final Data<Integer, Integer> d : observalObservableList) {
                // add drag event handler
                d.getNode().setOnMouseDragged(new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent e) {
                        final double sceneY = e.getSceneY();
                        final Axis<Integer> yAxis = lineChart.getYAxis();

                        final Number valueForDisplay = yAxis.getValueForDisplay(sceneY);

                        LOG.info("valueForDisplay {}", valueForDisplay);

                        d.setYValue(valueForDisplay.intValue());

                        e.consume();
                    }
                });
            }
        } catch (final JAXBException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void initTableView() {
        ObservableList<Series<Integer, Integer>> data = lineChart.getData();
        if (data == null || data.size() <= 0) {
            return;
        }
        final ObservableList<Data<Integer, Integer>> observalObservableList = data.get(0).getData();

        int i = 0;
        for (final TableColumn t : tableView.getColumns()) {
            switch (i) {
                case 0:
                    t.setCellValueFactory(new PropertyValueFactory<XYChart.Data, Integer>("XValue"));
                    break;
                case 1:
                    t.setCellValueFactory(new PropertyValueFactory<XYChart.Data, Integer>("YValue"));
                   /* t.setCellFactory(TextFieldTableCell.forTableColumn());
                    t.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<XYChart.Data, Integer>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<XYChart.Data, Integer> cellEditEvent) {
                            cellEditEvent.getTableView().getItems().get(
                                    cellEditEvent.getTablePosition().getRow())
                                    .setYValue(cellEditEvent.getNewValue().intValue());
                        }
                    });  */
            }

            i++;
        }

        tableView.setItems(observalObservableList);
    }
}
