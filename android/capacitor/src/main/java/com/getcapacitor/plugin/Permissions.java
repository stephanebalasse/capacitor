package com.getcapacitor.plugin;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import org.json.JSONArray;

@NativePlugin()
public class Permissions extends Plugin {

  @PluginMethod
  public void query(PluginCall call) {
    String name = call.getString("name");

    switch (name) {
      case "camera":
        checkCamera(call);
        break;
      case "photos":
        checkPhotos(call);
        break;
      case "geolocation":
        checkGeo(call);
        break;
      case "notifications":
        checkNotifications(call);
        break;
      case "clipboard-read":
      case "clipboard-write":
        checkClipboard(call);
      case "microphone":
        checkMicrophone(call);
        break;
      default:
        call.reject("Unknown permission type");
    }
  }

  private void checkPerm(String perm, PluginCall call) {
    JSObject ret = new JSObject();
    if (ContextCompat.checkSelfPermission(getContext(), perm) == PackageManager.PERMISSION_DENIED) {
      // Checks to see if permission has already been requested
      if(shouldShowRequestPermissionRationale((Activity) getContext(), perm) == true){
        ret.put("state", "denied");
      }else{
        ret.put("state", "prompt");
      }
    } else if (ContextCompat.checkSelfPermission(getContext(), perm) == PackageManager.PERMISSION_GRANTED) {
      ret.put("state", "granted");
    } else {
      ret.put("state", "prompt");
    }
    call.resolve(ret);
  }

  private void checkCamera(PluginCall call) {
    checkPerm(Manifest.permission.CAMERA, call);
  }

  private void checkPhotos(PluginCall call) {
    checkPerm(Manifest.permission.READ_EXTERNAL_STORAGE, call);
  }

  private void checkGeo(PluginCall call) {
    checkPerm(Manifest.permission.ACCESS_COARSE_LOCATION, call);
  }

  private void checkNotifications(PluginCall call) {
    boolean areEnabled = NotificationManagerCompat.from(getContext()).areNotificationsEnabled();
    JSObject ret = new JSObject();
    ret.put("state", areEnabled ? "granted" : "denied");
    call.resolve(ret);
  }

  private void checkClipboard(PluginCall call) {
    JSObject ret = new JSObject();
    ret.put("state", "granted");
    call.resolve(ret);
  }

  private void checkMicrophone(PluginCall call) {
    checkPerm(Manifest.permission.RECORD_AUDIO, call);
  }

}
