package no.bioforsk.vips.entity;

import java.util.Date;

/**
 * Data object that represents a weather observation. Could be measured (historical
 * and forecasted (future).
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author  Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class WeatherObservation implements Comparable{
   
    private String elementMeasurementTypeId;
    private Integer logIntervalId;
    private Date timeMeasured;
    private double value;
   
    /** Creates a new instance of WeatherObservation */
    public WeatherObservation() {
    }
        
    public void setTimeMeasured(Date timeMeasured) { this.timeMeasured = timeMeasured; }
    public Date getTimeMeasured() { return this.timeMeasured; }
    
    public void setElementMeasurementTypeId(String elementMeasurementTypeId) { 
        this.elementMeasurementTypeId = elementMeasurementTypeId; 
    }
    public String getElementMeasurementTypeId() { 
        return this.elementMeasurementTypeId; 
    }
   
    public void setLogIntervalId(Integer id) { this.logIntervalId = id.intValue(); }
    public Integer getLogIntervalId() { return this.logIntervalId; }
    
    /**
     * Compares by time measured in ascending order
     */
    public int compareTo(Object o) {
        WeatherObservation other = (WeatherObservation) o;
        return this.compareTo(other);
        
    }
    
    /**
     * Compares by time measured in ascending order
     * @param other the observation to compare to
     * @return
     */
    public int compareTo(WeatherObservation other)
    {
    	if(this.getTimeMeasured() == null && other.getTimeMeasured() == null)
            return 0;
        else if(this.getTimeMeasured() == null)
            return -1;
        else if(other.getTimeMeasured() == null)
            return 1;
        else
            return this.getTimeMeasured().compareTo(other.getTimeMeasured());
    }
    
    @Override
    public String toString()
    {
        return "Observation \n" +
                "elementMeasurementTypeId=" + this.getElementMeasurementTypeId() + "\n" +
                "timeMeasured=" + this.getTimeMeasured().toString() + "\n" +
                "value=" + this.getValue();
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }
}
