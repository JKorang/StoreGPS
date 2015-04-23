package com.rowansenior.storegps;

import java.util.*;

/**
 * Created by Engineering on 4/23/15.
 */
public interface Subject {
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObservers(int quanitity);
}
