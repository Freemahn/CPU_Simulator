package utils;

/**
 * @author Pavel Gordon
 */
public class Utils
{
    public static int[] reverseArray(int[] array)
    {
        for (int i = 0; i < array.length / 2; i++)
        {
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
        return array;
    }


    public static Integer[] reverseArray(Integer[] array)
    {
        for (int i = 0; i < array.length / 2; i++)
        {
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
        return array;
    }


    public static String formatArray(int[] a)
    {
        String str = "";
        for (int k = 0; k < a.length; k++)
        {
            str += String.format("%4d ", a[k]);
            if ((k + 1) % 10 == 0)
            {
                str += "\n";
            }

        }
        return str;
    }


    public static String formatArray(Integer[] a)
    {
        String str = "";
        for (int k = 0; k < a.length; k++)
        {
            str += String.format("%4d ", a[k]);
            if ((k + 1) % 10 == 0)
            {
                str += "\n";
            }

        }
        return str;
    }
}
