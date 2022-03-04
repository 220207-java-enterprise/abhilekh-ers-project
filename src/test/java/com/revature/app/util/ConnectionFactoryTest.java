package com.revature.app.util;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class ConnectionFactoryTest {

    @Test
    public void test_getConnection_returnsValidConnection_givenThatPropertiesFileExistsWithCorrectInfo(){
        try{
            assertNotNull(ConnectionFactory.getInstance().getConnection());
        } catch (Throwable t){
            fail();
        }
    }

}
