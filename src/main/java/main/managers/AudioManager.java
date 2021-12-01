package main.managers;

import exceptions.AudioNotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioManager {
    List<AudioFile> audioFiles;

    public AudioManager(List<AudioFile> audioFiles) {
        this.audioFiles = audioFiles;
        ServerManager.save();
    }
    public AudioManager(){
        this(new ArrayList<>());
    }

    public void addAudioFile(String path){
        audioFiles.add(new AudioFile(path));
        ServerManager.save();
    }
    public void removeAudioFile(String path){
        if(new File(path).delete()){
            audioFiles.remove(new AudioFile(path));
            ServerManager.save();
        }
    }
    public void removeAll(){
        for(AudioFile audioFile : audioFiles){
            String path = audioFile.getPath();
            new File(path).delete();
        }
        ServerManager.save();
    }

    public AudioFile getAudioFile(String path) throws AudioNotFoundException {
        for(AudioFile audioFile : audioFiles){
            if(audioFile.getPath().equals(path)){
                return audioFile;
            }
        }
        throw new AudioNotFoundException("Audio file not found");
    }

    public static class AudioFile{
        private String path;

        public AudioFile(String path){
            this.path = path;
        }
        public String getPath(){
            return path;
        }
        public void setPath(String path){
            this.path = path;
        }
        public String toString(){
            return path;
        }
    }
}
