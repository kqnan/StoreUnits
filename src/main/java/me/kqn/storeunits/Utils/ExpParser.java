package me.kqn.storeunits.Utils;

import org.nfunk.jep.JEP;

public class ExpParser {
    private static JEP jep=new JEP();
    /**
     * 输入的表达式不能包含未知数
     * */
    public static double parseMathExpression(String expression){
        jep.parseExpression(expression);
        return jep.getValue();
    }

}
