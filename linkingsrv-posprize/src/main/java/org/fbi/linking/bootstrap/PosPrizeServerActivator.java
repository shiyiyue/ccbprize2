/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.fbi.linking.bootstrap;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PosPrizeServerActivator implements BundleActivator {

    private static BundleContext context;
    private PosPrizeServerService posPrizeServerService;

    public static BundleContext getBundleContext()
    {
        return context;
    }

    public void start(BundleContext context) {
        PosPrizeServerActivator.context = context;
        try {
            this.posPrizeServerService = new PosPrizeServerService(getBundleContext());
            this.posPrizeServerService.start();
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - Starting the Pos prize server bundle....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(BundleContext context) throws Exception {
        this.posPrizeServerService.stop();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - Stopping the Pos prize server bundle...");
    }

}
