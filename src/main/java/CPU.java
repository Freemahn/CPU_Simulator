import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;
import utils.Utils;

import static utils.Utils.formatArray;

/**
 * @author Pavel Gordon
 */
public class CPU implements Runnable
{

    private String fileName;
    private int[] registers;
    /**
     * Number of cpu
     */
    private int number;

    private Stack<Integer> stack;
    /**
     * Program itself
     */
    private Instruction[] instructions;
    /**
     * Program counter - number of line to be executed next
     */
    private int pc;
    private boolean isEnded;
    private MemoryManager memoryManager;


    /**
     *
     * @param fileName path to file
     * @param number - number of cpu(1-4)
     * @param memoryManager
     * @throws IOException
     */
    public CPU(String fileName, int number, MemoryManager memoryManager) throws IOException
    {

        this.fileName = fileName;
        this.number = number;
        this.memoryManager = memoryManager;
        registers = new int[5];
        stack = new Stack<>();
        instructions = Files.lines(Paths.get(fileName))
            .map(CPU::parseInstruction)
            .filter(Objects::nonNull)
            .toArray(Instruction[]::new);

    }


    @Override
    public void run()
    {
        for (pc = 0; pc < instructions.length && !isEnded; pc++)
        {
            Instruction instruction = instructions[pc];
            execute(instruction);
        }

    }


    private void execute(Instruction instruction)
    {

        if (instruction == null)
        {
            return;
        }
//        System.out.println(instruction);
        switch (instruction.getCommand())
        {
            case ADD:
            {
                int addr1 = instruction.arg(0);
                int addr2 = instruction.arg(1);
                registers[addr1] += registers[addr2];
                break;
            }

            case MULT:
            {
                int addr1 = instruction.arg(0);
                int addr2 = instruction.arg(1);
                registers[addr1] *= registers[addr2];
                break;
            }

            case STORE:
            {
                int addr1 = instruction.arg(0);
                int memoryAddr = instruction.arg(1);
                memoryManager.store(registers[addr1], memoryAddr);
                break;
            }
            case FETCH:
            {
                int addr1 = instruction.arg(0);
                int memoryAddr = instruction.arg(1);
                registers[addr1] = memoryManager.fetch(memoryAddr);
                break;
            }
            case EQU:
            {
                int addr1 = instruction.arg(0);
                int addr2 = instruction.arg(1);
                int addr3 = instruction.arg(2);
                if (registers[addr1] == registers[addr2])
                {
                    registers[addr3] = 1;
                }
                else
                {
                    registers[addr3] = 0;
                }
                break;
            }

            case PUSH:
            {
                int addr1 = instruction.arg(0);
                stack.push(registers[addr1]);
                break;
            }

            case POP:
            {
                int addr1 = instruction.arg(0);
                registers[addr1] = stack.pop();
                break;
            }

            case GOTO:
            {
                int addr1 = instruction.arg(0);
                pc += registers[addr1];
                break;
            }

            case LOAD:
            {
                int addr1 = instruction.arg(0);
                int value = instruction.arg(1);
                registers[addr1] = value;
                break;
            }

            case OUT:
            {
                int addr1 = instruction.arg(0);
                dumpRegister(addr1);
                break;
            }
            case IF:
            {
                int addr1 = instruction.arg(0);
                int addr2 = instruction.arg(1);
                if (registers[addr1] == 1)
                {
                    pc += registers[addr2];
                }
                break;
            }

            case DEBUG:
            {
                int code = instruction.arg(0);
                switch (code)
                {
                    case 1:
                    {
                        dumpRegisters();
                        break;
                    }
                    case 2:
                    {
                        dumpMemory();
                        break;
                    }
                    case 3:
                    {
                        dumpStack();
                        break;
                    }
                    case 4:
                    {
                        dumpRegisters();
                        dumpMemory();
                        dumpStack();
                        break;
                    }
                }

                break;
            }

            case END:
            {
                isEnded = true;
                break;
            }
        }
    }


    private static Instruction parseInstruction(String str)
    {
        Scanner input = new Scanner(str);
        //tokens are seperated by commas and spaces
        input.useDelimiter("[,|\\s]+");
        String oc = input.next();
        int v1 = 0;
        int v2 = 0;
        int v3 = 0;
        String temp;
        //ignore comment lines
        if (str.length() > 1 && str.substring(0, 2).equals("//"))
        {
            return null;
        }
        //parse 1st argument. For registers, accept format 0 or R0
        if (input.hasNextInt())
        {
            v1 = input.nextInt();
        }
        else if (input.hasNext())
        {
            temp = input.next();
            if (temp.length() >= 2 &&
                temp.charAt(0) == 'R')
            {
                v1 = Integer.parseInt(temp.substring(1, 2));
            }

        }
        if (input.hasNextInt())
        {
            v2 = input.nextInt();
        }
        else if (input.hasNext())
        {
            temp = input.next();
            if (temp.length() >= 2 &&
                temp.charAt(0) == 'R')
            {
                v2 = Integer.parseInt(temp.substring(1, 2));
            }

        }
        else
        {
            return new Instruction(Instruction.Command.valueOf(oc), v1);
        }
        if (input.hasNextInt())
        {
            v3 = input.nextInt();
        }
        else if (input.hasNext())
        {
            temp = input.next();
            if (temp.length() >= 2 &&
                temp.charAt(0) == 'R')
            {
                v3 = Integer.parseInt(temp.substring(1, 2));
            }

        }
        else

        {
            return new Instruction(Instruction.Command.valueOf(oc), v1, v2);
        }
        return new Instruction(Instruction.Command.valueOf(oc), v1, v2, v3);
    }


    private void dumpRegisters()
    {
        String str = "cpu: " + number + ", registers: ";
        str += formatArray(registers);
        str += " pc=" + pc;
        System.out.println(str);
    }

    private void dumpRegister(int index)
    {
        String str = "cpu: " + number + ", registers: " + index + "=" + registers[index];
        System.out.println(str);
    }


    private void dumpMemory()
    {
        String str = "memoryManager: \n";
        str += formatArray(memoryManager.memory);
        System.out.println(str);
    }


    private void dumpStack()
    {
        String str = "cpu: " + number + ", stack: ";
        str += formatArray(Utils.reverseArray(stack.toArray(new Integer[0])));
        System.out.println(str);

    }



}


