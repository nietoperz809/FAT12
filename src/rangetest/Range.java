/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rangetest;

/**
 *
 * @author Administrator
 */
public class Range
{
    public long from;
    public long to;
    public Object host;
    
    /**
     * Builds a range
     * @param from Start
     * @param to End
     * @throws Exception end lesser than start or start is negative  
     */
    public Range(long from, long to, Object host) throws Exception
    {
        if (to < from)
            throw new Exception ("to < from");
        if (from < 0)
            throw new Exception ("negative");
        this.from = from;
        this.to = to;
        this.host = host;
    }

    /**
     * Length of range to - from
     * @return length
     */
    private long length ()
    {
        return to - from;
    }
    
    /**
     * Returns biggest of 2 ranges
     * @param other Other range
     * @return the bigger one
     */
    public Range getBigger (Range other)
    {
        if (other.length() > this.length())
            return other;
        return this;
    }

    /**
     * Returns smallest of 2 ranges
     * @param other Other range
     * @return the smaller one
     */
    public Range getSmaller (Range other)
    {
        if (other.length() < this.length())
            return other;
        return this;
    }
    
    /**
     * Returns true if both Ranges are equal length
     * @param other Other range
     * @return 
     */
    public boolean isEqualLength (Range other)
    {
        return this.length() == other.length();
    }

    @Override
    public boolean equals(Object other)
    {
        return this.length() == ((Range)other).length() &&
                this.host == ((Range)other).host;
    }

    /**
     * Returns true if both ranges overlap
     * @param other Other one 
     * @return 
     */
    public boolean overlap (Range other)
    {
        return this.from <= other.to && this.to >= other.from;
    }
    
    /**
     * Cobines two ranges to one
     * @param other Other range
     * @return new Range
     * @throws Exception if Range creation fails 
     */
    public Range combine (Range other) throws Exception
    {
        return new Range (Math.min(this.from, other.from), Math.max(this.to, other.to), this.host);
    }

    /**
     * Returns intersection of two Ranges
     * @param other Other range
     * @return intersection or NULL if ranges don't overlap
     * @throws Exception 
     */
    public Range intersect (Range other) throws Exception
    {
        if (overlap(other))
        {
            return new Range (Math.max(this.from, other.from), Math.min(this.to, other.to), this.host);
        }
        return null;
    }
    
    /**
     * Returns positive distance of both start values
     * @param other range
     * @return the distance
     */
    public long StartDistance (Range other)
    {
        long d = this.from - other.from;
        if (d < 0)
            return -d;
        return d;
    }
    
    /**
     * Returns new Range if both overlap
     * @param other Other range
     * @return new Range
     * @throws Exception if new range creation fails 
     */
    public Range combineIfOverlap (Range other) throws Exception
    {
        if (overlap(other))
            return combine(other);
        return null;
    }
    
    @Override
    public String toString()
    {
        String b = "(" + this.from +
                '/' +
                this.to + ')';
        return b;
    }
}

