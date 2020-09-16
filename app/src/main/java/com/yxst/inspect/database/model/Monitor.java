package com.yxst.inspect.database.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

/*
 MenuID	279
MenuNameCN	"一线循环风机"
URL	"http://60.164.211.4:9800/monitor/ZX_JC/熟料一线循环风机.svg"
 */
@Entity
public class Monitor {
    @Id
    private Long id;
   private Long MenuID;
   private String MenuNameCN;
   private String URL;
@Generated(hash = 458367026)
public Monitor(Long id, Long MenuID, String MenuNameCN, String URL) {
    this.id = id;
    this.MenuID = MenuID;
    this.MenuNameCN = MenuNameCN;
    this.URL = URL;
}
@Generated(hash = 1754530431)
public Monitor() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public Long getMenuID() {
    return this.MenuID;
}
public void setMenuID(Long MenuID) {
    this.MenuID = MenuID;
}
public String getMenuNameCN() {
    return this.MenuNameCN;
}
public void setMenuNameCN(String MenuNameCN) {
    this.MenuNameCN = MenuNameCN;
}
public String getURL() {
    return this.URL;
}
public void setURL(String URL) {
    this.URL = URL;
}


}
