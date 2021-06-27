package com.example.finalapplication.music;

/*Custom Thread Extends Thread Class to add Two Variables that control the seekbar. Either totalDuration And currentPosition*/
public class CustomThread extends Thread {
    public int totalDuration;
    public int currentPosition;

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
