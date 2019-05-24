package tree.redblack;


// 50以下是权限问题
public enum ColorEnums {

    BLACK("black"), RED("red");
    private String color;

    ColorEnums(String color) {
        this.color = color;
    }
}