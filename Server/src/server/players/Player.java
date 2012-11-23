/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.players;

import java.awt.*;
import java.io.*;
import server.checkers.BlackChecker;
import server.checkers.IChecker;
import server.checkers.WhiteChecker;

/**
 *
 * @author alina
 */
public class Player
{
    private int id;
    private ObjectInputStream reader; 
    private ObjectOutputStream writer; 
    private int checkersCount;
    private boolean connected;
    private int score;
    private Color color;
    private IChecker current_checker;
    private Point startPoint;    
    private IChecker[][] board;
    
    public Player(int id, Color color, ObjectInputStream reader, ObjectOutputStream writer)
    {
        this.id = id;
        score = 0;
        this.color = color;
        this.reader = reader;
        this.writer = writer;
        checkersCount = 0;
        connected = true;
        current_checker = null;
        //reader = rdr;
    }

    public void setBoard(IChecker[][] gameBoard)
    {
        board = gameBoard;
    }
  
    public Color getColor()
    {
        return color;
    }
    
    public IChecker getCurrentChecker()
    {
        return current_checker;
    }

    /**
     * @return the score
     */
    public int getScore()
    {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score)
    {
        this.score = score;
    }
    
    
    
    public boolean isAlive()
    {
        return (checkersCount > 0 && connected);
    }
    
    //Расстановка фигур на доске в начале игры
    public void SetCheckersInStartPosition()
    {
        for(int i = 0; i < board.length; i++) 
        {
            for(int j = 0; j < board.length; j++) 
            {
                if((i + j) % 2 == 1)
                {
                    if(j < 3 && color == Color.WHITE)
                    {
                        board[i][j] = new WhiteChecker(color, i, j, board);
                        checkersCount++;
                    }
                    if(j >= board.length - 3  && color == Color.BLACK)
                    {
                        board[i][j] = new BlackChecker(Color.BLACK, i, j, board);
                        checkersCount++;
                    }
                }
            }
        }    
    }
    
    //Проверка является ли шашка, шашкой игрока (по цвету)
    public boolean IsMineChecker(Point checker_coord)
    {
        if(board[checker_coord.x][checker_coord.y] != null && board[checker_coord.x][checker_coord.y].getColor() == color)
        {
            current_checker = board[checker_coord.x][checker_coord.y];
            return true;
        }
        else
        {
            return false;
        }
    }
    
    //Считывает из сокета запрос на перемещение шашки от клиента
    public Point ReadMoveRequest()
    {
        Integer x = null;
        Integer y = null;
        Integer x_new = null;
        Integer y_new = null;
        try
        {
            try
            {
                x = (Integer) reader.readObject();
                y = (Integer) reader.readObject();
                x_new = (Integer) reader.readObject();
                y_new = (Integer) reader.readObject();
                System.out.println("1. [" + x + ", " + y + "] -> [" + x_new + ", " + y_new + "]");
                /*String s = (String) reader.readObject();
                System.out.println(s);*/
            }
            catch (ClassNotFoundException ex)
            {
                System.out.println("ClassNotFoundException");
                connected = false;
                return null;
            }
        }
        catch (IOException e)
        {
            System.out.println("Stream " + id + " reading error");
            connected = false;
            return null;
        }
        if(x != null && y != null && x_new != null && y_new != null)
        {
            current_checker = board[x][y];
            return new Point(x_new, y_new);
        }
        else
            return null;
    }

    public void SendNewGameCommand(int color, String name, String opponentName)// + currentPoint
    {
        try
        {
                System.out.println("Player: " + name + ", Opponent: " + opponentName + ", color: " + color);
            writer.writeObject(color);
            writer.writeObject(name);
            writer.writeObject(opponentName);
            writer.flush();
        }
        catch(IOException e)
        {
            connected = false;
            System.out.println("Stream " + id + " writing error");            
        }
    }

    public void SendMoveCommand(Point startPoint, Point finishPoint, Point killedCheckerPoint, boolean turnToQueen, boolean endTurn)// + currentPoint
    {
        try
        {
                System.out.println("2. [" + startPoint.x + ", " + startPoint.y + "] -> [" + finishPoint.x + ", " + finishPoint.y + "]");
            writer.writeObject(startPoint.x);
            writer.writeObject(startPoint.y);
            writer.writeObject(finishPoint.x);
            writer.writeObject(finishPoint.y);
            writer.writeObject(killedCheckerPoint.x);
            writer.writeObject(killedCheckerPoint.y);
            writer.writeObject(turnToQueen);
            writer.writeObject(endTurn);
            writer.flush();
        }
        catch(IOException e)
        {
            connected = false;
            System.out.println("Stream " + id + " writing error");            
        }
    }
}