/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DutySchedulerApi;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 *
 * @author desrepair
 */
public class LocalDateInstanceCreator implements InstanceCreator<LocalDate> {
    @Override
    public LocalDate createInstance(Type type) {
        return LocalDate.MIN;
    }
}
