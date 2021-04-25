package me.Jakubok.nations.terrain;


import me.Jakubok.nations.administration.TeritorryClaimer;

public class RegisterChunkPos {

    protected ModChunkPos chunk;
    protected TeritorryClaimer belonging;

    public RegisterChunkPos(ModChunkPos chunk, TeritorryClaimer belonging) {
        this.chunk = chunk;
        this.belonging = belonging;
    }

    public ModChunkPos getChunk() {
        return chunk;
    }
    public TeritorryClaimer getBelonging() {
        return belonging;
    }
}
