package com.digibarber.app.Beans;

import java.io.Serializable;

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


}
