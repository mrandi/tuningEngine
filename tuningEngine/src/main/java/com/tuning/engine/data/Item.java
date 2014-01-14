/*
 * 13.04.2013 | 09:22:24
 * Marcel Wieczorek
 * marcel@wieczorek-it.de
 */
package com.tuning.engine.data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @author Marcel Wieczorek
 * @version 1.0
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item")
@XmlRootElement
public class Item implements Serializable {

    private static final long serialVersionUID = -977807666464708683L;

    @XmlElement
    private Integer key;
    @XmlElement
    private Integer value;

    public Integer getKey() {
	return key;
    }

    public void setKey(Integer key) {
	this.key = key;
    }

    public Integer getValue() {
	return value;
    }

    public void setValue(Integer value) {
	this.value = value;
    }
}
