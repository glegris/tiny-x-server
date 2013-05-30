#include <X11/Xlib.h>
#include <stdio.h>
#include <string.h>
 
#define NIL (0)
 
int main(int argc, char* argv[])
{
        Display *dpy = XOpenDisplay(NIL);
        if(!dpy)
        {
                printf("Failed to open the display.\n");
                return 0;
        }
 
        int blackColor = BlackPixel(dpy, DefaultScreen(dpy));
        int whiteColor = WhitePixel(dpy, DefaultScreen(dpy));
 
		XFontStruct * font3 = XLoadQueryFont (dpy, "fixed");
		if(!font3) 
		{
                printf("Failed to open font3.\n");
                return 0;
        }

        // Below you can see how to initalize a different color, it's pretty complicated, but there you go.
        XColor green, blue, exact;
        Colormap cmap = DefaultColormap(dpy, 0);
        XAllocNamedColor(dpy, cmap, "green",&green, &exact);
        XAllocNamedColor(dpy, cmap, "blue",&blue, &exact);
        int greenColor = green.pixel;
        int blueColor = blue.pixel;
 
        // Create the window
        Window w = XCreateSimpleWindow(dpy, DefaultRootWindow(dpy), 0, 0, 100, 100,3, whiteColor, blackColor);
        XStoreName(dpy, w, "Grab1");
 
		// A Center Window
        Window cw = XCreateSimpleWindow(dpy, w, 0, 0, 50, 50,5, whiteColor, greenColor);

		XSetWindowAttributes setwinattr;
		setwinattr.win_gravity = CenterGravity;
		unsigned long valuemask = CWWinGravity;
		XChangeWindowAttributes(dpy, cw, valuemask, &setwinattr);

        // Create a graphics context
        GC gc = XCreateGC(dpy, w, 0, NIL);
 
        // Tell the gc we draw using the white color
        XSetForeground(dpy, gc, whiteColor);
 
         // Send the requests to the server
        XFlush(dpy);
 
        XEvent event;
        XSelectInput(dpy, w, ExposureMask | ButtonPressMask | KeyPressMask);

        // "Map" the window  (make it appear on the screen)
        XMapWindow(dpy, w);
        XMapWindow(dpy, cw);

        while(!XNextEvent(dpy,&event))
        {
				printf("event  %d\n", event.type);	     
                if(event.type == Expose)
                {
						printf("Expose\n");	     
                        XClearWindow(dpy, w);
                        XClearWindow(dpy, cw);

                        // Send the requests to the server
                        XFlush(dpy);

						int r = XGrabKeyboard(dpy, cw, True, GrabModeAsync, GrabModeSync, CurrentTime);
						printf("grab status  %d\n", r);	     

                }
                else if(event.type == ButtonPress)
                {
						printf("button %d\n", event.xbutton.button);	   
                        // If you would like to get the coordinates of the mouse, you would create
                        // an XButtonPressedEvent like below, and then use the x and y fields of that event
                        //XButtonPressedEvent *bpEvent = &amp;event.xbutton;
                        if(event.xbutton.button == 1) break;

						XAllowEvents(dpy, SyncKeyboard, CurrentTime);
                }
                else if(event.type == KeyPress)
                {
                        printf("Keycode %d\n", event.xkey.keycode);	                        
				}
       }
        return 0;
}
