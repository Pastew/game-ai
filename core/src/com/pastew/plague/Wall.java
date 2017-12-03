/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pastew.plague;

import static com.pastew.plague.Vector2D.*;

/**
 *
 * @author User
 */
public class Wall {
    

    protected Vector2D m_vA = new Vector2D(),
            m_vB = new Vector2D(),
            m_vN = new Vector2D();

    protected void CalculateNormal() {
        Vector2D temp = Vec2DNormalize(sub(m_vB, m_vA));

        m_vN.x = -temp.y;
        m_vN.y = temp.x;
    }

    public Wall() {
    }

    public Wall(Vector2D A, Vector2D B) {
        m_vA = A;
        m_vB = B;
        CalculateNormal();
    }

    public Wall(Vector2D A, Vector2D B, Vector2D N) {
        m_vA = A;
        m_vB = B;
        m_vN = N;
    }



    public Vector2D From() {
        return m_vA;
    }

    public void SetFrom(Vector2D v) {
        m_vA = v;
        CalculateNormal();
    }

    public Vector2D To() {
        return m_vB;
    }

    public void SetTo(Vector2D v) {
        m_vB = v;
        CalculateNormal();
    }

    public Vector2D Normal() {
        return m_vN;
    }

    public void SetNormal(Vector2D n) {
        m_vN = n;
    }

    public Vector2D Center() {
        return div(add(m_vA, m_vB), 2.0);
    }


}
