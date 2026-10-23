package com.example.smartpantry.ui;

import android.content.*;import android.graphics.Color;import android.graphics.drawable.GradientDrawable;import android.view.*;import android.widget.*;

public final class Ui { private Ui(){} public static int green=Color.rgb(46,125,82), dark=Color.rgb(24,53,42), cream=Color.rgb(249,247,241), text=Color.rgb(38,52,44);
 public static LinearLayout page(Context c){LinearLayout p=new LinearLayout(c);p.setOrientation(LinearLayout.VERTICAL);p.setBackgroundColor(cream);return p;}
 public static TextView title(Context c,String s){TextView t=new TextView(c);t.setText(s);t.setTextColor(dark);t.setTextSize(28);t.setTypeface(null,1);t.setPadding(24,28,24,12);return t;}
 public static TextView label(Context c,String s){TextView t=new TextView(c);t.setText(s);t.setTextColor(text);t.setTextSize(15);t.setPadding(24,8,24,4);return t;}
 public static Button button(Context c,String s){Button b=new Button(c);b.setText(s);b.setTextColor(Color.WHITE);b.setTextSize(14);b.setAllCaps(false);b.setBackground(round(green,18));return b;}
 public static GradientDrawable round(int color,int radius){GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(radius);return g;}
 public static TextView empty(Context c,String s){TextView t=new TextView(c);t.setText(s);t.setTextColor(Color.DKGRAY);t.setTextSize(16);t.setGravity(Gravity.CENTER);t.setPadding(32,48,32,48);return t;}
 public static LinearLayout nav(Context c,View.OnClickListener pantry,View.OnClickListener suggested,View.OnClickListener settings){LinearLayout n=new LinearLayout(c);n.setPadding(8,8,8,8);n.setBackgroundColor(Color.WHITE);String[] labels={"Pantry","Suggested","Settings"};View.OnClickListener[] ls={pantry,suggested,settings};for(int i=0;i<3;i++){Button b=new Button(c);b.setText(labels[i]);b.setAllCaps(false);b.setTextColor(dark);b.setOnClickListener(ls[i]);n.addView(b,new LinearLayout.LayoutParams(0,56,1));}return n;}
}
