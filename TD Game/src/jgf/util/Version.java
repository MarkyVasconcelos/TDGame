/*
 */

package jgf.util;

/**
 * The <tt>Version</tt> class is the default class for version representation.
 * A version is formed by a <i>main version</i>, a <i>major version<i>, a
 * <i>minor version</i> and a beta version. The version is compatible with
 * another if both the main and major version are identical.
 * 
 * @author Vendramin, M?rcio
 * @author Mendonça, Vin?cius Godoy de
 */
public final class Version implements Comparable<Version>
{
    private int main;
    private int major;
    private int minor;

    private int beta;

    /**
     * Createa a new version.
     * 
     * @param main The main version.
     * @param major The major version.
     * @param minor The minor version.
     * @param beta If true, the version will be marked as beta 1. Otherwise, it
     *        will be an official version.
     */
    public Version(int main, int major, int minor, boolean beta)
    {
        this(main, major, minor, beta ? 1 : 0);
    }

    /**
     * Creates a new version.
     * 
     * @param main The main version.
     * @param major The major version.
     * @param minor The minor version.
     * @param beta True if is a beta, false if not.
     */
    public Version(int main, int major, int minor, int beta)
    {
        super();
        if (main < 0)
            throw new IllegalArgumentException(
                    "The main version must be greater than 0!");
        if (major < 0)
            throw new IllegalArgumentException(
                    "The major version must be greater than 0!");
        if (minor < 0)
            throw new IllegalArgumentException(
                    "The minor version must be greater than 0!");

        this.main = main;
        this.major = major;
        this.minor = minor;
        this.beta = beta;
    }

    /**
     * Creates a new official version.
     * 
     * @param main Main version
     * @param major Major version
     * @param minor Minor version.
     */
    public Version(int main, int major, int minor)
    {
        this(main, major, minor, 0);
    }

    /**
     * Creates a new official version. The minor part will be zero.
     * 
     * @param main Main version
     * @param major Major version
     */
    public Version(int main, int major)
    {
        this(main, major, 0, 0);
    }

    /**
     * Creates a new version based on a version string. Samples of version
     * strings are:
     * 
     * <pre>
     *      0.4    
     *      0.4 b 
     *      0.4 b 1
     *      0.4 beta 1
     *      1.2.3
     *      1.2.3 b 1
     *      1.2.3 beta
     *      1.2.3 beta 1
     * </pre>
     * 
     * @param version The string containing the version.
     * @throws IllegalArgumentException If the version string is invalid.
     */
    /*
     * size 2 0.4 size 3 1.2.3 1.2 b 1.2 beta size 4 1.2 b 1 1.2 beta 1 1.2.3 b
     * 1.2.3 beta size 5 1.2.3 b 1 1.2.3 beta 1
     */
    public Version(String version)
    {
        String pattern = "[0-9]+.[0-9]+(.[0-9]+)?( (beta|b)( [0-9]+)?)?";

        if (!version.matches(pattern))
            throw new IllegalArgumentException("Invalid version string!");

        String[] versionParts = version.split("[.\\s]");
        int len = versionParts.length;

        try
        {
            main = Integer.parseInt(versionParts[0]);
            major = Integer.parseInt(versionParts[1]);

            if (len == 2)
            {
                minor = 0;
                beta = 0;
                return;
            }

            setBeta(versionParts);

            setMinorIfSizeThree(versionParts);
            setMinorIfSizeFour(versionParts);
            setMinorIfSizeFive(versionParts);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Invalid version string", e);
        }
    }

    private void setBeta(String[] versionParts)
    {
        int len = versionParts.length;
        if ("b".equals(versionParts[len - 2])
                || "beta".equals(versionParts[len - 2]))
            beta = Integer.parseInt(versionParts[len - 1]);
        else if ("b".equals(versionParts[len - 1])
                || "beta".equals(versionParts[len - 1]))
            beta = 1;
    }

    private void setMinorIfSizeThree(String[] versionParts)
    {
        if (versionParts.length != 3)
            return;

        if (isBeta())
            minor = 0;
        else
            minor = Integer.parseInt(versionParts[2]);
    }

    private void setMinorIfSizeFour(String[] versionParts)
    {
        int len = versionParts.length;
        if (len != 4)
            return;

        if ("b".equals(versionParts[len - 1])
                || "beta".equals(versionParts[len - 1]))
            minor = Integer.parseInt(versionParts[2]);
        else
            minor = 0;
    }

    private void setMinorIfSizeFive(String[] versionParts)
    {
        if (versionParts.length != 5)
            return;

        minor = Integer.parseInt(versionParts[2]);
    }

    /**
     * Informs if it's a beta or official release.
     * 
     * @return True if is a beta, false if not.
     */
    public boolean isBeta()
    {
        return beta > 0;
    }

    /**
     * @return The main part of this version.
     */
    public int getMain()
    {
        return main;
    }

    /**
     * @return The major part of this version.
     */
    public int getMajor()
    {
        return major;
    }

    /**
     * @return The minor part of this version.
     */
    public int getMinor()
    {
        return minor;
    }

    /**
     * @return A string with the complete version. The complete version is in
     *         the format <tt>MAIN.MAJOR.MINOR beta</tt>
     */

    public String getComplete()
    {
        StringBuilder sb = new StringBuilder("" + main).append(".");
        sb.append(major).append(".").append(minor);
        if (isBeta())
            sb.append(" beta " + beta);
        return sb.toString();
    }

    /**
     * @return A string with the small version. The complete version is in the
     *         format <tt>MAIN.MAJOR b</tt>
     */
    public String getSmall()
    {
        StringBuilder sb = new StringBuilder("");
        sb.append(main).append(".").append(major);
        if (isBeta())
            sb.append(" b " + beta);

        return sb.toString();
    }

    @Override
    public String toString()
    {
        return getComplete();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;

        if (obj == this)
            return true;

        if (!(obj instanceof Version))
            return false;

        Version v = (Version) obj;

        return v.getMain() == getMain() && v.getMajor() == getMajor()
                && v.getMinor() == getMinor() && v.getBeta() == getBeta();
    }

    @Override
    public int hashCode()
    {
        return new HashBuilder(main).add(major).add(minor).add(beta).hashCode();
    }

    /**
     * Test if this object is compatible with the given version. It will be
     * considered compatible if the main and major versions are equal and if the
     * minor version of the compared element is bigger or equal this version.
     * 
     * @param v The version in which this must be compatible.
     * @return True if is compatible, false if not. If the v parameter is null,
     *         returns false.
     */
    public boolean isCompatible(Version v)
    {
        if (v == null)
            return false;

        return v.getMain() == getMain() && v.getMajor() == getMajor()
                && (v.isBigger(this) || v.equals(this));
    }

    /**
     * Test is this version is bigger then the given one.
     * 
     * @param version The version to be tested.
     * @return True if is this version is bigger and false if it is smaller.
     */
    public boolean isBigger(Version version)
    {
        if (main > version.main)
            return true;

        if (main < version.main)
            return false;

        if (major > version.major)
            return true;

        if (major < version.major)
            return false;

        if (minor > version.minor)
            return true;

        if (minor < version.minor)
            return false;

        if (isBeta() == true && version.isBeta() == false)
            return false;

        if (isBeta() == false && version.isBeta() == true)
            return true;

        if (beta < version.beta)
            return false;

        if (beta > version.beta)
            return true;

        return false;
    }

    public int getBeta()
    {
        return isBeta() ? beta : 0;
    }

    public int compareTo(Version o)
    {
        if (o.equals(this))
            return 0;

        return this.isBigger(o) ? 1 : -1;
    }
}
