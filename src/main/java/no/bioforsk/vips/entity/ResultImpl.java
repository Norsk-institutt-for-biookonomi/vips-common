package no.bioforsk.vips.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a result
 * @copyright 2013 {@link http://www.bioforsk.no Bioforsk}
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ResultImpl implements Result{
    private Date resultValidTime;
    private Set<String> keys;
    private HashMap<String, String> values;

    public void setResultValidTime(Date time) {
        this.resultValidTime = time;
    }

    public Date getResultValidTime() {
        return this.resultValidTime;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }

    public Set<String> getKeys() {
        return this.values.keySet();
    }

    public void setValue(String key, String value) {
        if(this.values == null)
            this.values = new HashMap();
        this.values.put(key, value);
    }

    public String getValue(String key) {
        return this.values != null ? this.values.get(key) : null;
    }

    public Map<String, String> getAllValues() {
        return this.values;
    }

    /**
     * Sorting by timestamp
     * @param t
     * @return 
     */
    public int compareTo(Object t) {
        return this.getResultValidTime().compareTo(((Result) t).getResultValidTime());
    }
    
}
