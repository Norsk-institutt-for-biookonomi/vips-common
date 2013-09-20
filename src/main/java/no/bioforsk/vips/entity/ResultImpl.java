package no.bioforsk.vips.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a result
 * @copyright 2013 <a href="http://www.bioforsk.no">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ResultImpl implements Result{
    private Date resultValidTime;
    private Set<String> keys;
    private HashMap<String, String> values;
    private Integer warningStatus;

    @Override
    public void setResultValidTime(Date time) {
        this.resultValidTime = time;
    }

    @Override
    public Date getResultValidTime() {
        return this.resultValidTime;
    }

    @Override
    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }

    @Override
    public Set<String> getKeys() {
        return this.values.keySet();
    }

    @Override
    public void setValue(String key, String value) {
        if(this.values == null)
            this.values = new HashMap();
        this.values.put(key, value);
    }

    @Override
    public String getValue(String key) {
        return this.values != null ? this.values.get(key) : null;
    }

    @Override
    public Map<String, String> getAllValues() {
        return this.values;
    }

    /**
     * Sorting by timestamp
     * @param t
     * @return 
     */
    @Override
    public int compareTo(Object t) {
        return this.getResultValidTime().compareTo(((Result) t).getResultValidTime());
    }

    @Override
    public void setWarningStatus(Integer warningStatus) {
        this.warningStatus = warningStatus;
    }

    @Override
    public Integer getWarningStatus() {
        return this.warningStatus != null ? this.warningStatus: WARNING_STATUS_NO_WARNING;
    }
    
}
