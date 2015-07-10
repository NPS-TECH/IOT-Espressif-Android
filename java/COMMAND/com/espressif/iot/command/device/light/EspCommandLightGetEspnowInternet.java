package com.espressif.iot.command.device.light;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.espressif.iot.base.api.EspBaseApiUtil;
import com.espressif.iot.type.device.status.EspStatusEspnow;
import com.espressif.iot.type.device.status.IEspStatusEspnow;
import com.espressif.iot.type.net.HeaderPair;

public class EspCommandLightGetEspnowInternet implements IEspCommandLightGetEspnowInternet
{

    @Override
    public List<IEspStatusEspnow> doCommandLightGetEspnowInternet(String deviceKey)
    {
        String headerKey = Authorization;
        String headerValue = Token + " " + deviceKey;
        JSONObject result = null;
        HeaderPair header = new HeaderPair(headerKey, headerValue);
        result = EspBaseApiUtil.Get(URL, header);
        if (result == null)
        {
            return null;
        }
        try
        {
            int status = result.getInt(Status);
            if (status != HttpStatus.SC_OK)
            {
                return null;
            }
            
            List<IEspStatusEspnow> list = new ArrayList<IEspStatusEspnow>();
            JSONArray switchArray = result.getJSONArray(Switches);
            for (int i = 0; i < switchArray.length(); i++)
            {
                JSONObject switchJSON = switchArray.getJSONObject(i);
                String mac = switchJSON.getString(Mac);
                int voltage = switchJSON.getInt(VoltageMV);
                
                IEspStatusEspnow espnowStatus = new EspStatusEspnow();
                espnowStatus.setMac(mac);
                espnowStatus.setVoltage(voltage);
                list.add(espnowStatus);
            }
            
            return list;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
}