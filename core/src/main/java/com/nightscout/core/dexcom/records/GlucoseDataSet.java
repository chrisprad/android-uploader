package com.nightscout.core.dexcom.records;

import com.nightscout.core.dexcom.TrendArrow;
import com.nightscout.core.dexcom.Utils;
import com.nightscout.core.model.GlucoseUnit;
import com.nightscout.core.model.SensorEntry;
import com.nightscout.core.model.SensorGlucoseValueEntry;
import com.nightscout.core.utils.GlucoseReading;

import org.joda.time.DateTime;

public class GlucoseDataSet {

    private DateTime systemTime;
    private DateTime displayTime;
    private DateTime wallTime;
    private GlucoseReading reading;
    private TrendArrow trend;
    private int noise;
    private long unfiltered;
    private long filtered;
    private int rssi;

    public GlucoseDataSet(SensorGlucoseValueEntry egvRecord, long receiverTime, long referenceTime) {
        systemTime = Utils.receiverTimeToDate(egvRecord.sys_timestamp_sec);
        displayTime = Utils.receiverTimeToDate(egvRecord.disp_timestamp_sec);
        wallTime = Utils.systemTimeToWallTime(egvRecord.sys_timestamp_sec, receiverTime, referenceTime);
        reading = new GlucoseReading(egvRecord.sgv_mgdl, GlucoseUnit.MGDL);
        trend = TrendArrow.values()[egvRecord.trend.ordinal()];
        noise = egvRecord.noise.ordinal();
    }

    public GlucoseDataSet(EGVRecord egvRecord) {
        systemTime = egvRecord.getSystemTime();
        displayTime = egvRecord.getDisplayTime();
        reading = egvRecord.getReading();
        trend = egvRecord.getTrend();
        noise = egvRecord.getNoiseMode().ordinal();
    }

    public GlucoseDataSet(EGVRecord egvRecord, SensorRecord sensorRecord) {
        this(egvRecord);
        // TODO check times match between record
        unfiltered = sensorRecord.getUnfiltered();
        filtered = sensorRecord.getFiltered();
        rssi = sensorRecord.getRssi();
    }

    public GlucoseDataSet(SensorGlucoseValueEntry egvRecord, SensorEntry sensorRecord, long receiverTimestamp, long referenceTime) {
        this.wallTime = Utils.systemTimeToWallTime(egvRecord.sys_timestamp_sec, receiverTimestamp, referenceTime);
        this.systemTime = Utils.receiverTimeToDate(egvRecord.sys_timestamp_sec);
        this.displayTime = Utils.receiverTimeToDate(egvRecord.disp_timestamp_sec);
        this.reading = new GlucoseReading(egvRecord.sgv_mgdl, GlucoseUnit.MGDL);
        this.trend = TrendArrow.values()[egvRecord.trend.ordinal()];
        this.noise = egvRecord.noise.ordinal();
        this.unfiltered = sensorRecord.unfiltered;
        this.filtered = sensorRecord.filtered;
        this.rssi = sensorRecord.rssi;
    }

    public DateTime getSystemTime() {
        return systemTime;
    }

    public DateTime getDisplayTime() {
        return displayTime;
    }

    public int getBgMgdl() {
        return reading.asMgdl();
    }

    public GlucoseReading getReading() {
        return reading;
    }

    public TrendArrow getTrend() {
        return trend;
    }

    public String getTrendSymbol() {
        return trend.symbol();
    }

    public int getNoise() {
        return noise;
    }

    public long getUnfiltered() {
        return unfiltered;
    }

    public long getFiltered() {
        return filtered;
    }

    public int getRssi() {
        return rssi;
    }

    public DateTime getWallTime() {
        return wallTime;
    }
}
