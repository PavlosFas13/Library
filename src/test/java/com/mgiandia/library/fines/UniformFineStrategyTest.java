package com.mgiandia.library.fines;

import java.time.LocalDate;

import org.junit.*;

import com.mgiandia.library.fines.FineStrategy;
import com.mgiandia.library.fines.UniformFineStrategy;
import com.mgiandia.library.util.Money;
import com.mgiandia.library.util.SimpleCalendar;


/**
 * 
 * @author ndia
 *
 */

public class UniformFineStrategyTest {

    private LocalDate dueDate;
    private FineStrategy strategy;
    private Money dailyFine;
    
    @Before
    public void setUp() {
        dueDate = FineTestCalendar.get1stMarchOf2007();
        strategy = new UniformFineStrategy();
        dailyFine = Money.euros(5);
    }
    
    
    @Test
    public void noDueDate() {
        Money fine = strategy.calculateFine(null, FineTestCalendar.get1stMarchOf2007(), dailyFine);
        Assert.assertEquals(Money.euros(0),fine);        
        
    }
    
    @Test
    public void noReturnDate() {
        Money fine = strategy.calculateFine(dueDate, null, dailyFine);
        Assert.assertEquals(Money.euros(0),fine);        
        
    }
    
    @Test
    public void returnSameDateAsDue() {
        Money fine = strategy.calculateFine(dueDate, FineTestCalendar.get1stMarchOf2007(), dailyFine);
        Assert.assertEquals(Money.euros(0),fine);        
    }
    
    
    @Test
    public void returnPreviousDate() { 
        Money fine = strategy.calculateFine(dueDate, FineTestCalendar.get28thFebruaryOf2007(), dailyFine);
        Assert.assertEquals(Money.euros(0),fine);
    }
    
    @Test
    public void returnNextDate() {
        Money fine = strategy.calculateFine(dueDate, FineTestCalendar.get2ndMarchOf2007(), dailyFine);    
        Assert.assertEquals(dailyFine, fine);
    }
    
    @Test
    public void returnEndOfTheSameWeek() {
        Money fine = strategy.calculateFine(dueDate, FineTestCalendar.get4thMarchOf2007(), dailyFine);    
        Assert.assertEquals(dailyFine.times(3), fine);
        
    }
    
    @Test
    public void returnFirstDayOfTheNextWeek() {
        Money fine = strategy.calculateFine(dueDate, FineTestCalendar.get5thMarchOf2007(), dailyFine);    
        Assert.assertEquals(dailyFine.times(4), fine);        
    }
    
    
    @Test
    public void returnLastDayOfTheNextWeek() {
        Money fine = strategy.calculateFine(dueDate, FineTestCalendar.get11thMarchOf2007(), dailyFine);    
        Assert.assertEquals(dailyFine.times(10), fine);                
    }
}
