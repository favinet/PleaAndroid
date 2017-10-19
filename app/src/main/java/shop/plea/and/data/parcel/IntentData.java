package shop.plea.and.data.parcel;

import android.os.Parcel;
import android.os.Parcelable;

import shop.plea.and.data.config.Constants;

/**
 * Created by shimtaewoo on 2017-10-02.
 */

public class IntentData implements Parcelable {
    public int aniType;
    public String authId;
    public String joinType;
    /*public String station_cd;

    public int isPopup;
    public String link;
    public String params;
    public String totalSaving;
    public String adroute;
    public String cobjid;
    public boolean isCard;
    public String checkCardno;
    public boolean needTitle;
    public boolean isModify;
    public int signStep;
    public boolean needBack;
    public String event_url;
    public String deepLinkUri;
    public boolean isFromGift;
    public String title;*/

    public IntentData(){
        aniType = Constants.VIEW_ANIMATION.ANI_NONE;
        authId = "";
        joinType = "";
    }

    protected IntentData(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in)
    {
        aniType = in.readInt();
        authId = in.readString();
        joinType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(aniType);
        dest.writeString(authId);
        dest.writeString(joinType);
        /*dest.writeString(station_cd);
        dest.writeString(sns_id);
        dest.writeString(sns);
        dest.writeInt(isPopup);
        dest.writeString(link);
        dest.writeString(params);
        dest.writeString(totalSaving);
        dest.writeString(adroute);
        dest.writeString(cobjid);
        dest.writeByte((byte) (isCard ? 1 : 0));
        dest.writeString(checkCardno);
        dest.writeByte((byte) (needTitle ? 1 : 0));
        dest.writeByte((byte) (isModify ? 1 : 0));
        dest.writeInt(signStep);
        dest.writeByte((byte) (needBack ? 1 : 0));
        dest.writeString(event_url);
        dest.writeString(deepLinkUri);
        dest.writeByte((byte) (isFromGift ? 1 : 0));
        dest.writeString(title);*/
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IntentData> CREATOR = new Creator<IntentData>() {
        @Override
        public IntentData createFromParcel(Parcel in) {
            return new IntentData(in);
        }

        @Override
        public IntentData[] newArray(int size) {
            return new IntentData[size];
        }
    };
}

