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
        XStoreName(dpy, w, "Gravity");
 
		// A NorthWest Window
        Window wnw = XCreateSimpleWindow(dpy, w, 0, 0, 20, 20,5, whiteColor, greenColor);

		// A North Window
        Window wn = XCreateSimpleWindow(dpy, w, 50, 0, 20, 20,5, whiteColor, blueColor);
		XSetWindowAttributes setwinattr;
		setwinattr.win_gravity = NorthGravity;
		unsigned long valuemask = CWWinGravity;
		XChangeWindowAttributes(dpy, wn, valuemask, &setwinattr);

        // Create a graphics context
        GC gc = XCreateGC(dpy, w, 0, NIL);
 
        // Tell the gc we draw using the white color
        XSetForeground(dpy, gc, whiteColor);
 
         // Send the requests to the server
        XFlush(dpy);
 
        XEvent event;
        XSelectInput(dpy, w, ExposureMask | ButtonPressMask);

        // "Map" the window  (make it appear on the screen)
        XMapWindow(dpy, w);
        XMapWindow(dpy, wnw);
        XMapWindow(dpy, wn);

        while(!XNextEvent(dpy,&event))
        {
                if(event.type == Expose)
                {
                        XClearWindow(dpy, w);
                        XClearWindow(dpy, wnw);

                        // Send the requests to the server
                        XFlush(dpy);
                }
                else if(event.type == ButtonPress)
                {
                        // If you would like to get the coordinates of the mouse, you would create
                        // an XButtonPressedEvent like below, and then use the x and y fields of that event
                        //XButtonPressedEvent *bpEvent = &amp;event.xbutton;
                        break;
                }
        }
        return 0;
}
