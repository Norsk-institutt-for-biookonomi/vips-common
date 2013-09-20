package no.bioforsk.vips.entity;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Represents a result from a model run
 * @copyright 2013 {@link http://www.bioforsk.no Bioforsk}
 * @author Tor-Einar Skog <tor-einar.skog@hyper.no>
 */
public interface Result extends Comparable{
    
    public final static Integer WARNING_STATUS_NO_WARNING = 0;
    public final static Integer WARNING_STATUS_NO_WARNING_MISSING_DATA = 1;
    public final static Integer WARNING_STATUS_NO_RISK = 2;
    public final static Integer WARNING_STATUS_MINOR_RISK = 3;
    public final static Integer WARNING_STATUS_HIGH_RISK = 4;

    /* Time for which these results are valid */
    public void setResultValidTime(Date time);
    public Date getResultValidTime();
    
    /* The result parameters in this Result object */
    public void setKeys(Set<String> keys);
    public Set<String> getKeys();
    
    /* Setting and getting values */
    public void setValue(String key, String value);
    public String getValue(String key);
    public Map<String,String> getAllValues();
    
    public void setWarningStatus(Integer warningStatus);
    public Integer getWarningStatus();
}
