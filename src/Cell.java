public class Cell {
//function to check if the input is Number
    public static boolean isNumber (String s) {
        boolean ans = true;
        try {
            double d = Double.parseDouble(s);
        }
        catch (Exception e) {
            ans = false;
        }
        return ans;
    }
    //function to check if the input is Text
    public static boolean isText(String s){
        boolean ans=false;
        try {
            double d=Double.parseDouble(s);

        }
        catch (Exception e){
            ans=true;
        }
        return ans;
    }
    public static boolean isForm(String s){

    }
}
