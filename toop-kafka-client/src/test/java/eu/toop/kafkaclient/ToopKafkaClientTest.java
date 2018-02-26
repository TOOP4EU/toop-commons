/**
 * Copyright (C) 2018 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.kafkaclient;

import org.apache.kafka.common.KafkaException;
import org.junit.jupiter.api.Test;

/**
 * Test class for class {@link ToopKafkaClient}.
 *
 * @author Philip Helger
 */
public final class ToopKafkaClientTest {
  @Test
  public void testBasic () {
    try {
      ToopKafkaClient.setEnabled (true);
      // Don't send too many - will take forever if no Kafka server is up and
      // running!
      for (int i = 0; i < 5; ++i)
        ToopKafkaClient.send ("Key" + i, "Value" + i);
      ToopKafkaClient.close ();
    } catch (final KafkaException ex) {
      // lets act as if we are not surprised...
    } finally {
      // Disable again for other tests
      ToopKafkaClient.setEnabled (false);
    }
  }
}
