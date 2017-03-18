package co.edu.udea.compumovil.gr07_20171.lab2;

import android.graphics.*;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;



public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {

    OnHeadlineSelectedListener mCallback;
    private String[] titles;
    private String[] details;
    private byte[][] images;
    private float[] scores;
    String[] datos;
    String[] dates;
    String[] managers;
    String[] locations;
    String [] ids;
    public RecyclerAdapter(String[] titulos,String[] detalles, byte[][] imagenes,float[] puntuaciones,String[] fechas,String[] administrador, String[] ubicaciones ,String [] i){
        titles=titulos;
        details=detalles;
        images=imagenes;
        scores =puntuaciones;
        dates= fechas;
        managers= administrador;
        locations=ubicaciones;
        ids = i;
        datos = new String[8];
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemGeneral;
        public TextView itemManager;
        public TextView itemDate;
        public TextView itemLocate;
        public RatingBar itemPuntos;
        public TextView IDs;



        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemGeneral =(TextView)itemView.findViewById(R.id.item_general);
            itemManager =(TextView)itemView.findViewById(R.id.item_manager);
            itemDate =(TextView)itemView.findViewById(R.id.item_date);
            itemLocate =(TextView)itemView.findViewById(R.id.item_locate);
            itemPuntos =(RatingBar)itemView.findViewById(R.id.item_puntos);
            IDs = (TextView) itemView.findViewById(R.id.item_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    datos[0] = itemTitle.getText().toString();
                    datos[1] = itemGeneral.getText().toString();
                    datos[2] = itemManager.getText().toString();
                    datos[3] = itemDate.getText().toString();
                    datos[4] = itemLocate.getText().toString();
                    datos[5] = itemPuntos.getRating()+"";
                    datos[6] = "Events";
                    datos[7] = IDs.getText().toString();
                    mCallback.onArticleSelected(datos);
                }
            });
        }
    }

    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onArticleSelected(String datos[]);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) recyclerView.getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException(recyclerView.getContext().toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titles[i]);
        viewHolder.itemGeneral.setText(details[i]);
        viewHolder.itemManager.setText(managers[i]);
        viewHolder.itemDate.setText(dates[i]);
        viewHolder.itemLocate.setText(locations[i]);
        Bitmap bmp = BitmapFactory.decodeByteArray(images[i],0,images[i].length);
        viewHolder.itemImage.setImageBitmap(bmp);
        viewHolder.itemPuntos.setRating(scores[i]);
        viewHolder.IDs.setText(ids[i]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }



}