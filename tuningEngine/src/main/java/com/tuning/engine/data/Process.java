/*
 * 13.04.2013 | 09:14:55
 * Marcel Wieczorek
 * marcel@wieczorek-it.de
 */
package com.tuning.engine.data;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Marcel Wieczorek
 * @version 1.0
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "person")
@XmlRootElement
public class Process implements Serializable {

    private static final long serialVersionUID = -8130492140562197988L;

    @XmlElement
    private List<Item> items;

    public List<Item> getItems() {
	if (items == null) {
	    items = Lists.newArrayList();
    }
	return items;
    }

}
