/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.checkers;

import java.awt.*;

/**
 *
 * @author alina
 */
public final class WhiteChecker extends Checker
{

    public WhiteChecker(Color color, int x, int y, IChecker[][] masChecker)
    {
        super(color, x, y, masChecker);
    }

    //Может ли шашка сделать данных ход
    @Override
    public boolean CheckMotion(Point targetPoint)
    {
        if (targetPoint.y <= y)
        {
            return false;
        }

        int dX = Math.abs(targetPoint.x - x), dY = targetPoint.y - y;

        if (dX == 2 && dY == 2
                && board[(targetPoint.x + x) / 2][(targetPoint.y + y) / 2] != null
                && board[(targetPoint.x + x) / 2][(targetPoint.y + y) / 2].getColor() != Color.BLACK)
        {
            return true;
        }
        if (dX == 1 && dY == 1)
        {
            return true;
        }

        return false;
    }

    //Проверяет, может ли шашка убить вражескую
    @Override
    public boolean CanKillSmb()
    {
        if (y + 2 < board.length)
        {
            if (x - 2 > -1 && board[x - 1][y + 1] != null && board[x - 1][y + 1].getColor() == Color.BLACK)
            {
                if (board[x - 2][y + 2] == null)
                {
                    System.out.println("[" + getX() + ", " + getY() + "]" + "can kill " + "[" + (x - 1) + ", " + (y + 1) + "]");
                    return true;
                }
            }
            if (x + 2 < board.length && board[x + 1][y + 1] != null && board[x + 1][y + 1].getColor() == Color.BLACK)
            {
                if (board[x + 2][y + 2] == null)
                {
                    System.out.println("[" + getX() + ", " + getY() + "]" + "can kill " + "[" + (x + 1) + ", " + (y + 1) + "]");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean IfCanTurnToQueen()
    {
        return y == board.length - 1;
    }

    @Override
    public boolean IfThisOneKillSmb(Point targetPoint)
    {
        if (targetPoint.y - y != 2)
        {
            return false;
        }

        Point tmp = new Point((targetPoint.x + x) / 2, (targetPoint.y + y) / 2);
        if (board[tmp.x][tmp.y] != null && board[tmp.x][tmp.y].getColor() == Color.BLACK)
        {
            return true;
        }

        return false;
    }
}
