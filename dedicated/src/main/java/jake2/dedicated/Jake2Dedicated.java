package jake2.dedicated;

import jake2.qcommon.Com;
import jake2.qcommon.Defines;
import jake2.qcommon.Globals;
import jake2.qcommon.exec.Cbuf;
import jake2.qcommon.exec.Cmd;
import jake2.qcommon.exec.Cvar;
import jake2.qcommon.filesystem.FS;
import jake2.qcommon.longjmpException;
import jake2.qcommon.network.NET;
import jake2.qcommon.network.Netchan;
import jake2.qcommon.sys.Sys;
import jake2.qcommon.sys.Timer;
import jake2.server.SV_INIT;
import jake2.server.SV_MAIN;

import java.util.Arrays;
import java.util.List;

import static jake2.qcommon.MainCommon.*;

public class Jake2Dedicated {

    public static void main(String[] args) {


        Globals.dedicated = Cvar.getInstance().Get("dedicated", "1", Defines.CVAR_NOSET );

        // in C the first arg is the filename
        int argc = args.length + 1;
        String[] c_args = new String[argc];
        c_args[0] = "Jake2";
        if (argc > 1) {
            System.arraycopy(args, 0, c_args, 1, argc - 1);
        }
        try {

            // prepare enough of the subsystems to handle
            // cvar and command buffer management
            List<String> args1 = Arrays.asList(c_args);

            Cmd.Init();
            Cvar.getInstance().Init();

            // we need to add the early commands twice, because
            // a basedir or cddir needs to be set before execing
            // config files, but we want other parms to override
            // the settings of the config files
            Cbuf.AddEarlyCommands(args1, false);
            Cbuf.Execute();

            FS.InitFilesystem();

            Cbuf.reconfigure(args1, false);

            FS.setCDDir(); // use cddir from config.cfg

            Cbuf.reconfigure(args1, true); // reload default.cfg and config.cfg

            //
            // init commands and vars

            Globals.host_speeds= Cvar.getInstance().Get("host_speeds", "0", 0);
            Globals.log_stats= Cvar.getInstance().Get("log_stats", "0", 0);
            Globals.developer= Cvar.getInstance().Get("developer", "0", Defines.CVAR_ARCHIVE);
            Globals.timescale= Cvar.getInstance().Get("timescale", "0", 0);
            Globals.fixedtime= Cvar.getInstance().Get("fixedtime", "0", 0);
            Globals.logfile_active= Cvar.getInstance().Get("logfile", "0", 0);
            Globals.showtrace= Cvar.getInstance().Get("showtrace", "0", 0);
            Cvar.getInstance().Get("version", "1.0.0", Defines.CVAR_SERVERINFO | Defines.CVAR_NOSET);

            NET.Init();	//ok
            Netchan.Netchan_Init();	//ok

            SV_INIT.SV_Init();	//ok

            // add + commands from command line
            if (Cbuf.AddLateCommands(args1)) {
                // if the user didn't give any commands, run default action
                  if (Globals.dedicated.value == 0)
                      Cbuf.AddText("d1\n");
                  else
                      Cbuf.AddText("dedicated_start\n");

                Cbuf.Execute();
            }

            Com.Printf("====== Quake2 Initialized ======\n\n");

            // save config when configuration is completed

        } catch (longjmpException e) {
            Sys.Error("Error during initialization");
        }

        Globals.nostdout = Cvar.getInstance().Get("nostdout", "0", 0);

        int oldtime = Timer.Milliseconds();
        while (true) {
            // find time spending rendering last frame
            int newtime = Timer.Milliseconds();
            int time = newtime - oldtime;

            if (time > 0) {
                try {

                    debugLogStatsFile();

                    int adjustedTime = adjustTime(time);

                    debugLogTraces();

                    Cbuf.Execute();

                    SV_MAIN.SV_Frame(adjustedTime);

                } catch (longjmpException e) {
                    Com.DPrintf("longjmp exception:" + e);
                }
            }
            oldtime = newtime;
        }
    }
}
