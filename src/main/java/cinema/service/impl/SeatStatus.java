
package cinema.service.impl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


public enum SeatStatus {

    @XmlEnumValue("free")
    FREE("free"),
    @XmlEnumValue("locked")
    LOCKED("locked"),
    @XmlEnumValue("reserved")
    RESERVED("reserved"),
    @XmlEnumValue("sold")
    SOLD("sold");
    private final String value;

    SeatStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SeatStatus fromValue(String v) {
        for (SeatStatus c: SeatStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
