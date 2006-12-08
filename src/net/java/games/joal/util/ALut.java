package net.java.games.joal.util;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import net.java.games.joal.*;

/**
 * @author Athomas Goldberg
 *
 */
public final class ALut {

  private static ALC alc;
  private static ALCdevice device;
  private static ALCcontext context;
  private static Thread initializingThread;

  private ALut() { }

  /** Initializes the OpenAL Utility Toolkit, creates an OpenAL
      context and makes it current on the current thread. The ALut may
      only be initialized on one thread at any given time. */
  public static synchronized void alutInit() throws ALException {
    if (context != null) {
      throw new ALException("Already initialized on thread " + initializingThread.getName());
    }
    if (alc == null) {
      alc = ALFactory.getALC();
    }
    String deviceName = null;
    ALCdevice d = alc.alcOpenDevice(deviceName);
    if (d == null) {
      throw new ALException("Error opening default OpenAL device");
    }
    ALCcontext c = alc.alcCreateContext(d, null);
    if (c == null) {
      alc.alcCloseDevice(d);
      throw new ALException("Error creating OpenAL context");
    }
    alc.alcMakeContextCurrent(c);
    if (alc.alcGetError(d) != 0) {
      alc.alcDestroyContext(c);
      alc.alcCloseDevice(d);
      throw new ALException("Error making OpenAL context current");
    }
    // Fully initialized; finish setup
    device = d;
    context = c;
    initializingThread = Thread.currentThread();
  }

  /** Shuts down the OpenAL Utility Toolkit; releases and destroys the
      internal OpenAL context and closes the output device. Must be
      called from the same thread as alutInit(). Most applications
      should not need to call this; only those which wish to toggle
      sound on / off at run time by initializing and un-initializing
      OpenAL need to call it. */
  public static synchronized void alutExit() throws ALException {
    if (context == null) {
      throw new ALException("Not initialized");
    }
    alc.alcMakeContextCurrent(null);
    alc.alcDestroyContext(context);
    alc.alcCloseDevice(device);
    context = null;
    device = null;
    initializingThread = null;
  }

  public static void alutLoadWAVFile(String fileName,
                                     int[] format,
                                     ByteBuffer[] data,
                                     int[] size,
                                     int[] freq,
                                     int[] loop) throws ALException {
    try {
      WAVData wd = WAVLoader.loadFromFile(fileName);
      format[0] = wd.format;
      data[0] = wd.data;
      size[0] = wd.size;
      freq[0] = wd.freq;
      loop[0] = wd.loop ? AL.AL_TRUE : AL.AL_FALSE;
    } catch (Exception e) {
      throw new ALException(e);
    }
  }

  public static void alutLoadWAVFile(InputStream stream,
                                     int[] format,
                                     ByteBuffer[] data,
                                     int[] size,
                                     int[] freq,
                                     int[] loop) throws ALException {
    try {
      if (!(stream instanceof BufferedInputStream)) {
        stream = new BufferedInputStream(stream);
      }
      WAVData wd = WAVLoader.loadFromStream(stream);
      format[0] = wd.format;
      data[0] = wd.data;
      size[0] = wd.size;
      freq[0] = wd.freq;
      loop[0] = wd.loop ? AL.AL_TRUE : AL.AL_FALSE;
    } catch (Exception e) {
      throw new ALException(e);
    }
  }
}
