/*
 * Copyright (c) 2014 Geodevv.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License.
 */

package com.geodevv.testing.sampledomain.impl.audi;

import javax.inject.Inject;
import javax.inject.Named;

import com.geodevv.testing.sampledomain.Engine;
import com.geodevv.testing.sampledomain.Vehicle;

@Named("AudiA4")
public class AudiA4 implements Vehicle {

    private Engine engine;

    @Inject
    public AudiA4(@Named("AudiA4-engine") Engine engine) {
        this.engine = engine;
    }

    @Override
    public Engine getEngine() {
        return engine;
    }

    @Override
    public void start() {
        engine.turnOn();
    }
}
