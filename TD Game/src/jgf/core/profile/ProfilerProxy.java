package jgf.core.profile;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Shows how many time (in miliseconds) each method of a given interface took to
 * process.
 * 
 * @author Vinícius
 */
public class ProfilerProxy implements InvocationHandler
{
    private Object proxied;
    private Map<Method, Long> counters;
    private Map<Method, Double> sums;

    public double getFromMap(Map<Method, Double> map, Method method)
    {
        if (map.get(method) == null)
            return 0;

        return map.get(method).doubleValue();
    }

    public long getFromMapLong(Map<Method, Long> map, Method method)
    {
        if (map.get(method) == null)
            return 0;

        return map.get(method).longValue();
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable
    {
        long timeBefore = System.nanoTime();

        Object ret = method.invoke(proxied, args);

        double sum = getFromMap(sums, method);
        long counter = getFromMapLong(counters, method);

        sum += ((double) (System.nanoTime() - timeBefore)) / 1000000;

        if (++counter == 100)
        {
            System.out.printf("%s = %.4f\n", method.getName(), (sum / 100));
            sum = 0;
            counter = 0;
        }

        sums.put(method, new Double(sum));
        counters.put(method, new Long(counter));

        return ret;
    }

    private ProfilerProxy(Object proxied)
    {
        this.proxied = proxied;
        counters = new HashMap<Method, Long>();
        sums = new HashMap<Method, Double>();
    }

    public static Object newProfiled(Object obj)
    {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new ProfilerProxy(obj));
    }

}
