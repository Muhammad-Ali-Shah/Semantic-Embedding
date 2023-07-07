public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {
        //TODO Task 1.1
        doubElements = _elements;
    }

    public double getElementatIndex(int _index) {
        //TODO Task 1.2
        try
        {
            return doubElements[_index];
        }

        catch (IndexOutOfBoundsException e)
        {
            return -1;
        }
    }

    public void setElementatIndex(double _value, int _index) {
        //TODO Task 1.3
        try
        {
            doubElements[_index] = _value;
        }

        catch (IndexOutOfBoundsException e)
        {
            doubElements[doubElements.length - 1] = _value;
        }
    }

    public double[] getAllElements() {
        //TODO Task 1.4
        return doubElements;
    }

    public int getVectorSize() {
        //TODO Task 1.5
        return doubElements.length;
    }

    public Vector reSize(int _size) {
        //TODO Task 1.6
        int currentVectorSize = doubElements.length;
        if (_size == currentVectorSize || _size <= 0)
        {
            return this;
        }
        else
        {
            double[] newDoubElements = new double[_size];
            if (_size < currentVectorSize)
            {
                for (int i = 0; i < _size; i++)
                {
                    newDoubElements[i] = doubElements[i];
                }
            }
            else
            {
                for (int i = 0; i < currentVectorSize; i++)
                {
                    newDoubElements[i] = doubElements[i];
                }
                for (int i = currentVectorSize; i < _size; i++)
                {
                    newDoubElements[i] = -1.0;
                }
            }
            return new Vector(newDoubElements);
        }
    }

    public Vector add(Vector _v) {
        //TODO Task 1.7
        //change size
        int _vSize = _v.getVectorSize();
        Vector currentVector = this;
        int currentVectorSize = currentVector.doubElements.length;

        if (_vSize > currentVectorSize) {
            currentVector = currentVector.reSize(_vSize);
            currentVectorSize = _vSize;
        }
        else
        {
            _v = _v.reSize(currentVectorSize);
        }

        double[] newDoubElements = new double[currentVectorSize];
        for (int i = 0; i < currentVectorSize; i++)
        {
            newDoubElements[i] = currentVector.doubElements[i] + _v.getElementatIndex(i);
        }

        return new Vector(newDoubElements);
    }

    public Vector subtraction(Vector _v) {
        //TODO Task 1.8
        //change size
        int _vSize = _v.getVectorSize();
        Vector currentVector = this;
        int currentVectorSize = currentVector.doubElements.length;

        if (_vSize > currentVectorSize)
        {
            currentVector = currentVector.reSize(_vSize);
            currentVectorSize = _vSize;
        }
        else
        {
            _v = _v.reSize(currentVectorSize);
        }

        double[] newDoubElements = new double[currentVectorSize];
        for (int i = 0; i < currentVectorSize; i++)
        {
            newDoubElements[i] = currentVector.doubElements[i] - _v.getElementatIndex(i);
        }

        return new Vector(newDoubElements);
    }

    public double dotProduct(Vector _v) {
        //TODO Task 1.9
        //change size
        int _vSize = _v.getVectorSize();
        Vector currentVector = this;
        int currentVectorSize = currentVector.doubElements.length;

        if (_vSize > currentVectorSize)
        {
            currentVector = currentVector.reSize(_vSize);
            currentVectorSize = _vSize;
        }
        else
        {
            _v = _v.reSize(currentVectorSize);
        }

        double result = 0;
        for (int i = 0; i < currentVectorSize; i++)
        {
            result += (currentVector.doubElements[i] * _v.getElementatIndex(i));
        }
        return result;
    }

    public double cosineSimilarity(Vector _v) {
        //TODO Task 1.10
        //change size
        int _vSize = _v.getVectorSize();
        Vector currentVector = this;
        int currentVectorSize = currentVector.doubElements.length;

        if (_vSize > currentVectorSize)
        {
            currentVector = currentVector.reSize(_vSize);
            currentVectorSize = _vSize;
        }
        else
        {
            _v = _v.reSize(currentVectorSize);
        }

        double dotProductResult = 0;
        double sumOfSquaresOfCurrent = 0;
        double sumOfSquaresOf_v = 0;
        for (int i = 0; i < currentVectorSize; i++)
        {
            double elementAtIndexOfCurrent = currentVector.doubElements[i];
            double elementAtIndexOf_v = _v.getElementatIndex(i);
            dotProductResult += elementAtIndexOfCurrent * elementAtIndexOf_v;
            sumOfSquaresOfCurrent += elementAtIndexOfCurrent * elementAtIndexOfCurrent;
            sumOfSquaresOf_v += elementAtIndexOf_v * elementAtIndexOf_v;
        }
        return dotProductResult / (Math.sqrt(sumOfSquaresOfCurrent) * Math.sqrt(sumOfSquaresOf_v));
    }

    @Override
    public boolean equals(Object _obj) {
        Vector v = (Vector) _obj;
        boolean boolEquals = true;
        //TODO Task 1.11
        if (v.getVectorSize() != this.doubElements.length)
        {
            return false;
        }

        for (int i = 0; i < this.doubElements.length; i++)
        {
            if (v.getElementatIndex(i) != this.doubElements[i])
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < this.getVectorSize(); i++) {
            mySB.append(String.format("%.5f", doubElements[i])).append(",");
        }
        mySB.delete(mySB.length() - 1, mySB.length());
        return mySB.toString();
    }
}
