package com.udacity.stockhawk;

/**
 * Created by Mohamed AbdelraZek on 3/29/2017.
 */

public class MessageEvent {
    public String mSymbol;
    public String mMessage;
   public MessageEvent(String message,String symbol){
       this.mMessage=message;
       this.mSymbol=symbol;

   }
}

