package com.digibarber.app.Beans;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by DIGIBARBER LTD on 22/10/17.
 */

public class SelectedImages implements Serializable {

    public String image;
    public int SelectedTickPos;

    public SelectedImages(String image,
                          int SelectedTickPos) {
        this.image = image;
        this.SelectedTickPos = SelectedTickPos;

    }

    @Override
    public String toString() {
        return "SelectedImages{" +
                "image='" + image + '\'' +
                ", SelectedTickPos=" + SelectedTickPos +
                '}';
    }
}
