package project.bridgetek.com.applib.main.bean;

public class DropDown {
    private String OptionValue;
    private String OptionName;
    private String OptionStatus;
    private boolean select;

    public DropDown() {
    }

    public DropDown(String optionValue, String optionName, String optionStatus, boolean select) {
        OptionValue = optionValue;
        OptionName = optionName;
        OptionStatus = optionStatus;
        this.select = select;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getOptionValue() {
        return OptionValue;
    }

    public void setOptionValue(String optionValue) {
        OptionValue = optionValue;
    }

    public String getOptionName() {
        return OptionName;
    }

    public void setOptionName(String optionName) {
        OptionName = optionName;
    }

    public String getOptionStatus() {
        return OptionStatus;
    }

    public void setOptionStatus(String optionStatus) {
        OptionStatus = optionStatus;
    }
}
