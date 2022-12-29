package cinequiz.backend.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "List of multiple choice questions")
public class ListOfMCQ {
    @ApiModelProperty("The list of mcq")
    private List<MCQQuestion> list;

    @ApiModelProperty("Type of the mcq")
    private String type;

    @ApiModelProperty("The number of mcq")
    private Integer number;

    @ApiModelProperty("The language of the mcq")
    private String language;

    public ListOfMCQ() {
    }

    public ListOfMCQ(List<MCQQuestion> list, String type, Integer number, String language) {
        this.list = list;
        this.type = type;
        this.number = number;
        this.language = language;
    }

    public List<MCQQuestion> getList() {
        return list;
    }

    public void setList(List<MCQQuestion> list) {
        this.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
