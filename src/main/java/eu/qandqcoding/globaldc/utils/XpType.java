package eu.qandqcoding.globaldc.utils;

import java.util.Random;

public enum XpType {
     MESSAGE("Message", 10);

     public String name;
     public int xp;

     XpType(String name, int xp) {
          this.name = name;
          this.xp = xp;
     }

     public String getName() {
          return name;
     }

     public int getXp() {
          return xp;
     }
}
