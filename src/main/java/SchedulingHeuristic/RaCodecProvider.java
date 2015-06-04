/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchedulingHeuristic;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 *
 * @author desrepair
 */
public class RaCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> type, CodecRegistry cr) {
        if (type == Ra.class) {
            return (Codec<T>) new RaCodec(cr);
        }
        return null;
    }
    
}
