
package cinema.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


public class Seat {

    @XmlElement(name = "Row", required = true, nillable = true)
    protected String row;
    @XmlElement(name = "Column", required = true, nillable = true)
    protected String column;


    public String getRow() {
        return row;
    }


    public void setRow(String value) {
        this.row = value;
    }


    public String getColumn() {
        return column;
    }


    public void setColumn(String value) {
        this.column = value;
    }

}
