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
    private float[] puntos;
    String[] datos;
    String [] ids;
    public RecyclerAdapter(String[] titulos,String[] detalles, byte[][] imagenes,float[] puntuaciones, String [] i){
        titles=titulos;
        details=detalles;
        images=imagenes;
        puntos=puntuaciones;
        ids = i;
        datos = new String[6];
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;
        public RatingBar itemPuntos;
        public TextView IDs;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail =(TextView)itemView.findViewById(R.id.item_detail);
            itemPuntos =(RatingBar)itemView.findViewById(R.id.item_puntos);
            IDs = (TextView) itemView.findViewById(R.id.item_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    datos[0] = itemTitle.getText().toString();
                    datos[1] = itemDetail.getText().toString();
                    datos[2] = itemDetail.getText().toString();
                    datos[3] = itemPuntos.getRating()+"";
                    datos[4] = "sitios";
                    datos[5] = IDs.getText().toString();
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
        viewHolder.itemDetail.setText(details[i]);
        //byte [] imagen = cursorPlace.getBlob(cursorPlace.getColumnIndex(BaseDeDatos.Column_Place.FOTO));
        Bitmap bmp = BitmapFactory.decodeByteArray(images[i],0,images[i].length);
        viewHolder.itemImage.setImageBitmap(bmp);
        viewHolder.itemPuntos.setRating(puntos[i]);
        viewHolder.IDs.setText(ids[i]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }



}