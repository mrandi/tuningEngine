package com.tuning.engine;

import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DragHandler implements EventHandler<MouseEvent> {

    private final Logger log = LoggerFactory.getLogger(DragHandler.class);

    private final XYChart.Data<String, Integer> data;
    private final LineChart<String, Number> lineChart;

    public DragHandler(final XYChart.Data<String, Integer> data, final LineChart<String, Number> lineChart) {
        this.data = data;
        this.lineChart = lineChart;
    }

    @Override
    public void handle(MouseEvent e) {
        final double zero = e.getSceneX();
        final int y = (int) (zero - e.getSceneY());

        log.info("y {}", e.getY());
        log.info("scene y {}", e.getSceneY());
        log.info("screen y {}", e.getScreenY());

        log.info("zero {}", zero);

        data.setYValue(y);

        // should not pass event to the parent
        e.consume();
    }
}