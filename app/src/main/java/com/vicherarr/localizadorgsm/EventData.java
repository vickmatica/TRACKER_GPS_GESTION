package com.vicherarr.localizadorgsm;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;


/**
 *
 * @author Víctor
 */
public class EventData implements Serializable {
    private static final long serialVersionUID = 1L;
    protected EventDataPK eventDataPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double latitude;
    private Double longitude;
    private Integer gpsAge;
    private Double speedKPH;
    private Double heading;
    private Double altitude;
    private String transportID;
    private Integer inputMask;
    private Integer outputMask;
    private Integer seatbeltMask;
    private String address;
    private String dataSource;
    private String rawData;
    private Double distanceKM;
    private Double odometerKM;
    private Double odometerOffsetKM;
    private Integer geozoneIndex;
    private String geozoneID;
    private Integer creationTime;

    public EventData() {
    }

    public EventData(EventDataPK eventDataPK) {
        this.eventDataPK = eventDataPK;
    }

    public EventData(String accountID, String deviceID, int timestamp, int statusCode) {
        this.eventDataPK = new EventDataPK(accountID, deviceID, timestamp, statusCode);
    }

    public EventDataPK getEventDataPK() {
        return eventDataPK;
    }

    public void setEventDataPK(EventDataPK eventDataPK) {
        this.eventDataPK = eventDataPK;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getGpsAge() {
        return gpsAge;
    }

    public void setGpsAge(Integer gpsAge) {
        this.gpsAge = gpsAge;
    }

    public Double getSpeedKPH() {
        return speedKPH;
    }

    public void setSpeedKPH(Double speedKPH) {
        this.speedKPH = speedKPH;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public String getTransportID() {
        return transportID;
    }

    public void setTransportID(String transportID) {
        this.transportID = transportID;
    }

    public Integer getInputMask() {
        return inputMask;
    }

    public void setInputMask(Integer inputMask) {
        this.inputMask = inputMask;
    }

    public Integer getOutputMask() {
        return outputMask;
    }

    public void setOutputMask(Integer outputMask) {
        this.outputMask = outputMask;
    }

    public Integer getSeatbeltMask() {
        return seatbeltMask;
    }

    public void setSeatbeltMask(Integer seatbeltMask) {
        this.seatbeltMask = seatbeltMask;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public Double getDistanceKM() {
        return distanceKM;
    }

    public void setDistanceKM(Double distanceKM) {
        this.distanceKM = distanceKM;
    }

    public Double getOdometerKM() {
        return odometerKM;
    }

    public void setOdometerKM(Double odometerKM) {
        this.odometerKM = odometerKM;
    }

    public Double getOdometerOffsetKM() {
        return odometerOffsetKM;
    }

    public void setOdometerOffsetKM(Double odometerOffsetKM) {
        this.odometerOffsetKM = odometerOffsetKM;
    }

    public Integer getGeozoneIndex() {
        return geozoneIndex;
    }

    public void setGeozoneIndex(Integer geozoneIndex) {
        this.geozoneIndex = geozoneIndex;
    }

    public String getGeozoneID() {
        return geozoneID;
    }

    public void setGeozoneID(String geozoneID) {
        this.geozoneID = geozoneID;
    }

    public Integer getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Integer creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventDataPK != null ? eventDataPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventData)) {
            return false;
        }
        EventData other = (EventData) object;
        if ((this.eventDataPK == null && other.eventDataPK != null) || (this.eventDataPK != null && !this.eventDataPK.equals(other.eventDataPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clienterest.EventData[ eventDataPK=" + eventDataPK + " ]";
    }
    
}

