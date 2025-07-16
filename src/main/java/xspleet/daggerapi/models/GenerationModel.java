package xspleet.daggerapi.models;

import java.util.List;

public class GenerationModel
{
    public List<LoottableModel> generation;

    public void setGeneration(List<LoottableModel> generation) {
        this.generation = generation;
    }

    public List<LoottableModel> getGeneration() {
        return generation;
    }
}
