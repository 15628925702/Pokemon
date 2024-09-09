package org.example.pokemon.model;

import java.util.ArrayList;
import java.util.List;

public class Spirit {
    List<Integer> SpiritIDs = new ArrayList<>();

    public void addID(int id) {
        SpiritIDs.add(id);
    }

    public List<Integer> getSpiritIDs() {
        return SpiritIDs;
    }
}
