/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.utilities.services.localping;

import io.cloudslang.content.utilities.entities.LocalPingInputs;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_RECEIVED;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_SENT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PERCENTAGE_PACKETS_LOST;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TIMEOUT_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_AVG;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MAX;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MIN;
import static org.junit.Assert.assertEquals;

public class SunOsPingCommandTest {

    private static final String SUN_OS_COMMAND_OUTPUT = "PING 10.13.14.220: 10 data bytes\n" +
            "18 bytes from pab.emea.cpqcorp.net (10.26.51.44): icmp_seq=0. time=186.036 ms\n" +
            "18 bytes from pab.emea.cpqcorp.net (10.26.51.44): icmp_seq=1. time=185.389 ms\n" +
            "18 bytes from pab.emea.cpqcorp.net (10.26.51.44): icmp_seq=2. time=186.500 ms\n" +
            "18 bytes from pab.emea.cpqcorp.net (10.26.51.44): icmp_seq=4. time=186.562 ms\n" +
            "\n" +
            "----10.26.51.44 PING Statistics----\n" +
            "5 packets transmitted, 4 packets received, 20% packet loss\n" +
            "round-trip (ms)  min/avg/max/stddev = 185.389/186.122/186.562/0.542\n";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCreateCommandWithTimeout() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .packetCount("5")
                .packetSize("100")
                .timeout("30000")
                .build();

        LocalPingCommand sunOsPingCommand = new SunOsPingCommand();
        String command = sunOsPingCommand.createCommand(localPingInputs);

        assertEquals("ping 10.13.14.220 30", command);
    }

    @Test
    public void testCreateCommandWithOtherArgs() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .packetCount("5")
                .packetSize("100")
                .build();

        LocalPingCommand sunOsPingCommand = new SunOsPingCommand();
        String command = sunOsPingCommand.createCommand(localPingInputs);

        assertEquals("ping -s 10.13.14.220 100 5", command);
    }

    @Test
    public void testCreateCommandThrowsInvalidTimeoutException() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .timeout("invalid")
                .build();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(TIMEOUT_SHOULD_HAVE_A_NUMERIC_VALUE);

        new SunOsPingCommand().createCommand(localPingInputs);
    }

    @Test
    public void testCreateCommandThrowsInvalidPacketCountException() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .packetCount("invalid")
                .build();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE);

        new SunOsPingCommand().createCommand(localPingInputs);
    }

    @Test
    public void testCreateCommandThrowsInvalidPacketSizeException() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .packetSize("invalid")
                .build();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE);

        new SunOsPingCommand().createCommand(localPingInputs);
    }

    @Test
    public void testParseOutput() {
        Map<String, String> resultsMap = new SunOsPingCommand().parseOutput(SUN_OS_COMMAND_OUTPUT);

        assertEquals("5", resultsMap.get(PACKETS_SENT));
        assertEquals("4", resultsMap.get(PACKETS_RECEIVED));
        assertEquals("20", resultsMap.get(PERCENTAGE_PACKETS_LOST));
        assertEquals("185.389", resultsMap.get(TRANSMISSION_TIME_MIN));
        assertEquals("186.122", resultsMap.get(TRANSMISSION_TIME_AVG));
        assertEquals("186.562", resultsMap.get(TRANSMISSION_TIME_MAX));
    }
}