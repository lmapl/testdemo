package algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ma peiliang
 * Create Date: 2018/7/6 15:56
 * Description: ${DESCRIPTION}
 */
public class MaxSubStringMain
{

    public static void main(String[] args){
        Map<Character,Element> characterElementMap = new HashMap<>();
        String sta = "abeddaebenjemnaemnemnanmepnqme";

        char[] characters = sta.toCharArray();
        for(int i=0;i<characters.length;i++){
            char elementChar = characters[i];
            Element element = characterElementMap.get(elementChar);
            if(element == null){
                Element newElement = new Element();
                newElement.setCharacter(elementChar);
                newElement.setMin(i);
                newElement.setLast(i);
                characterElementMap.put(elementChar,newElement);
                continue;
            }
            Integer max = element.getMax();
            if(max == null){
                element.setMax(i);
                element.setLast(i);
                characterElementMap.put(elementChar,element);
                continue;
            }
            if(i - element.getLast() > max - element.getMin()){
                element.setMin(element.getLast());
                element.setMax(i);
                element.setLast(i);
                characterElementMap.put(elementChar,element);
            }else{
                element.setLast(i);
                characterElementMap.put(elementChar,element);
            }
        }

        int subStringStart = -1;
        int subStringLength = -1;
        for (Map.Entry<Character,Element> entry : characterElementMap.entrySet())
        {
            Element element = entry.getValue();
            if(element.getMax() == null){
                element.setMax(characters.length);
            }

            int charMaxLength;
            int charMaxStart;
            if(element.getMax() - element.getMin() > characters.length -element.getLast()){
                charMaxLength = element.getMax() - element.getMin();
                charMaxStart = element.getMin();
            }else{
                charMaxLength = characters.length -element.getLast();
                charMaxStart = element.getLast();
            }

            if(charMaxLength > subStringLength){
                subStringLength = charMaxLength;
                subStringStart = charMaxStart;
            }
        }

        System.out.println(sta.substring(subStringStart,subStringStart+subStringLength));

    }

}

class Element{
    private Character character;

    private Integer min;

    private Integer max;

    private Integer last;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Integer getLast() {
        return last;
    }

    public void setLast(Integer last) {
        this.last = last;
    }
}
