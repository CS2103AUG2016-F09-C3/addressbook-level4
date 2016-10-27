package seedu.flexitrack.commons.events.storage;

import seedu.flexitrack.commons.events.BaseEvent;

public class DataStoragePathChangeEvent extends BaseEvent {
    public String newPath;

    public DataStoragePathChangeEvent(String path) {
        this.newPath = path;
    }

    @Override
    public String toString() {
        return newPath.toString();
    }
}
