package org.ros.address;

/**
 * Created by calderpg on 2/17/17.
 */
public class LimitedPortRangeProvider
{
    private static final int lower_bound_port_ = 3000;
    private static final int upper_bound_port_ = 30000;
    private int current_port_number_ = lower_bound_port_;

    private static LimitedPortRangeProvider provider_;

    private LimitedPortRangeProvider() {}

    public static LimitedPortRangeProvider getInstance()
    {
        if (provider_ == null)
        {
            provider_ = new LimitedPortRangeProvider();
        }
        return provider_;
    }

    private boolean testPortAvailable(int port_number_to_test)
    {
        System.out.println("Testing if port " + port_number_to_test + " is available...");
        boolean available = true;
        if (available)
        {
            System.out.println("...port " + port_number_to_test + " is available");
            return true;
        }
        else
        {
            System.out.println("...port " + port_number_to_test + " is NOT available");
            return false;
        }
    }

    public int getOpenPort()
    {
        System.out.println("Attempting to generate a limited-range port number...");
        while (current_port_number_ <= upper_bound_port_)
        {
            int current_port_to_test = current_port_number_;
            current_port_number_++;
            if (testPortAvailable(current_port_to_test))
            {
                System.out.println("...returning port " + current_port_to_test + " as an available port");
                return current_port_to_test;
            }
        }
        Double new_double = null;
        return (int)new_double.doubleValue();
    }
}